(ns markiki.core
  (:require [clojure.tools.cli :refer [cli]]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure-watch.core :refer [start-watch]]
            [cheshire.core :refer [generate-string]]
            [me.raynes.fs :as fs]
            [cpath-clj.core :as cp]
            [hiccup.core :refer :all]
            [clj-yaml.core :as yaml]
            [hiccup.page :refer :all])
  (:gen-class))


(def invalid-path-error
  "[ERROR] Make sure you provided a valid path. Are all the folders in place?")


(defn usage [options-summary]
  (->> ["Markiki - a simple static markdown personal wiki."
        ""
        "Usage: markiki [options] /path/to/source/markdown/files/"
        ""
        "Options:"
        options-summary
        ""
        "What to put into the source folder you provide:"
        "  * .md files into the '_posts' folder to be transformed into html"
        "  * into '_posts': a folder for every category (categories can be nested as needed)"
        "  * an optional '_config.yml' file"
        ""
        "HTML files to serve will be put into the '_output' folder."
        ""
        "Please refer to the README for more info: https://github.com/ff-/markiki"]
       (string/join \newline)))


(def default-config
  {:title "Markiki - Your Markdown Wiki"
   :description "This is a test description, replace it in the config file!\n\nYou can *use markdown* of course :)"})


(defn exit
  "Exits with status leaving a message"
  [status msg]
  (println msg)
  (System/exit status))


(defn split-title
  "Separates the main title from a Markdown text - returns a vector [title text]"
  [text]
  (->> (string/split-lines text)
       (reduce (fn [partial line]
                 (let [title (first partial)
                       text (second partial)]
                   (if (and (re-find (re-pattern "^# .*") line)
                            (empty? title))
                     [(second (string/split line #"# ")) text]
                     [title (str text "\n" line)])))
               ["" ""])))


(defn pathize
  "Takes a string, returns a trimmed down string only w/ alphabet and hyphens"
  [title]
  (-> title
      (string/replace #"[ -]{1,}" "-")
      (string/replace #"[^a-zA-Z-]" "")
      (string/split #"-")
      ((fn [s] (string/join "-" s))) ;; Because join takes the coll as last arg
      string/lower-case))


(defn parse-tree
  "Given a path scan for md files, and return a list. If folder, recur."
  [path category]
  (let [json (atom [])]
    (doseq [f (fs/list-dir path)]
      ;; This is ugly. Here to avoid mapping of reserved filenames
      (when-not (some #{(fs/base-name f)} ["_static" "_config.yml"])
        (swap! json
               conj
               (if (fs/directory? f)
                 (let [cat-name (fs/name f)
                       [new-cat new-path] (map #(str %1 "/" cat-name)
                                               [category path])]
                   (parse-tree new-path new-cat))
                 (let [[title content] (split-title (slurp f))
                       name (fs/name f)
                       ext (fs/extension f)]
                   (when (= ext ".md")
                     {:title title
                      :last-modified (fs/mod-time f)
                      :path (str category "/" (pathize name))
                      :text content }))))))
    @json))


(defn generate-data
  "Given the source and the config paths generates the map with all the data"
  [src-path config-path]
  (merge default-config
         (if (fs/exists? config-path)
           (yaml/parse-string (slurp config-path))
           {})
         {:articles (->> (parse-tree src-path "")
                         flatten
                         (remove nil?))}))


(defn generate-json
  "Given the source map with the data it spits a json in the output path"
  [wiki out-path]
  (spit (str out-path "/markiki.json")
        (generate-string wiki
                         {:pretty true})))


(defn generate-index
  "Writes the index.html in the _output folder"
  [path data]
  (spit (str path "/index.html")
        (html5 [:head
                [:meta {:charset "utf-8"}]
                [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
                [:meta {:name "viewport"
                        :content "width=device-width, initial-scale=1"}]
                [:meta {:name "description" :content ""}]
                [:meta {:name "author" :content ""}]
                [:link {:rel "icon" :href "webres/favicon.ico"}]
                [:title (:title data)]
                (include-css "webres/bootstrap.min.css")
                (include-css "https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css")
                (include-css "webres/markiki.css")]
               [:body
                [:div#app.container
                 [:i {:class "fa fa-cog fa-spin fa-5x"
                      :style "margin-top:3em;"}]]
                (include-js "webres/markiki.js")
                (include-js "https://code.jquery.com/jquery-2.1.4.min.js")
                (include-js "webres/bootstrap.min.js")
                [:script "window.onload = function(){markiki.core.main();}"]])))


;; Credits to http://stackoverflow.com/questions/28645436/idiomatic-clojure-to-copy-resources-from-running-jar-to-outside
(defn extract-dir-from-jar
  "Takes a dir name inside the jar and a destination dir, and copies the res-dir
  to the to out-dir."
  [res-dir out-dir]
  (doseq [[path uris] (cp/resources (io/resource res-dir))
          :let [uri (first uris)
                relative-path (subs path 1)
                output-file (io/file out-dir relative-path)]]
    (with-open [in (io/input-stream uri)]
      (io/copy in output-file))))


(defn -main
  "Parse the command line args (and check for errors), then generate the json.
  Then copy all the resources into the output folder."
  [& args]
  (let [[options arguments summary] (cli args
                                         ["-h" "--help" "Print this help"
                                          :default false
                                          :flag true]
                                         ["-w" "--watch" "Watch the folder for changes"
                                          :default false
                                          :flag true])
        path        (first arguments)
        src-path    (str path "/_posts")
        out-path    (str path "/_output")
        static-path (str path "/_static")
        config-path (str path "/_config.yml")]
    ;; Handle help and error conditions
    ;; The user should provide valid folders
    (cond
     (:help options)                        (exit 0 (usage summary))
     (not path)                             (exit 1 (usage summary))
     ;; (not (fs/exists? config-path))         (exit 1 invalid-path-error)
     (not (fs/directory? src-path))         (exit 1 invalid-path-error))
    ;; Start generating!
    (let [wiki (generate-data src-path config-path)]
      (println "[OK] Generating wiki...")
      (fs/delete-dir out-path)
      (fs/mkdir out-path)
      (generate-index out-path wiki)
      (generate-json wiki out-path)
      (doseq [p ["webres" "fonts"]]
        (let [res-dir (str out-path "/" p)]
          (fs/mkdir res-dir)
          (extract-dir-from-jar p res-dir)))
      ;; Copy the static resources
      (fs/copy-dir static-path out-path)
      (println "[OK] Done generating.")
      (when (:watch options)
        (start-watch [{:path src-path
                       :event-types [:create :modify :delete]
                       :bootstrap (fn [path] (println "[OK] Starting to watch " src-path))
                       :callback (fn [event filename]
                                   (println "[OK] Changes detected " event filename)
                                   (generate-json (generate-data src-path config-path)
                                                  out-path))
                       :options {:recursive true}}])))))
