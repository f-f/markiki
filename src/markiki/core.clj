(ns markiki.core
  (:require [clojure.tools.cli :refer [cli]]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure-watch.core :refer [start-watch]]
            [cheshire.core :refer :all]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all])
  (:gen-class))

(def invalid-path-error
  "[ERROR] Make sure you provided a valid path as an argument")

(defn usage [options-summary]
  (->> ["Markiki - a simple static markdown personal wiki."
        ""
        "Usage: markiki [options] /path/to/markdown/files/"
        ""
        "Options:"
        options-summary
        ""
        "What to put into the folder you provide:"
        "  * .md files to be transformed into html"
        "  * a folder for every category (categories can be nested as needed)"
        ""
        "Files will be put into the output/ folder."
        ""
        "Please refer to the repo for more information: https://github.com/ff-/markiki"]
       (string/join \newline)))

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

(defn parse-tree
  "Given a path scan for md files, and return a list. If folder, recur."
  [path category]
  (let [json (atom [])]
    (doseq [f (.listFiles (io/file path))]
      ;; This is ugly. Here to avoid mapping the static folder
      (when-not (some #{(.getName f)} ["static"])
        (swap! json
               conj
               (if (.isDirectory f)
                 (let [cat-name (.getName f)
                       [new-cat new-path] (map #(str %1 "/" cat-name) [category path])]
                   {:category new-cat
                    :articles (parse-tree new-path new-cat)})
                 (let [[title content] (split-title (slurp (.getPath f)))]
                   {:title title
                    :lastModified (.lastModified f)
                    :text content})))))
  @json))

(defn generate-wiki
  "Given a OS path it will explore the folder tree and write a json in path/out/"
  [path]
  (spit (str path "/out/markiki.json")
        (generate-string (parse-tree (str path "/src") "") {:pretty true})))

(defn copy-css
  "Copies the css from the jar resources to the out/ folder"
  [path]
  (->> "markiki.css"
       io/resource
       io/file
       slurp
       (spit (str path "/out/markiki.css"))))

(defn generate-index
  "Writes the index.html in out/"
  [path]
  (spit (str path "/out/index.html")
        (html5 [:head
                (include-css "markiki.css")
                [:title "Markiki - Your Markdown Wiki"]
                [:meta {:http-equiv "Content-Type"
                        :content "text/html; charset=utf-8"}]]
               [:body
                (include-js "markiki.js")
                [:div
                 [:h1.info "Home"]]])))

(defn -main [& args]
  (let [[options arguments summary] (cli args
                                         ["-h" "--help" "Print this help"
                                          :default false
                                          :flag true]
                                         ["-w" "--watch" "Watch the folder for changes"
                                          :default false
                                          :flag true])
        path (first arguments)
        src-path (str path "/src")]
    ;; Handle help and error conditions
    ;; The user should provide a valid directory
    (cond
     (:help options) (exit 0 (usage summary))
     (not path) (exit 1 (usage summary))
     (not (.isDirectory (io/file src-path))) (exit 1 invalid-path-error))
    ;; Start generating!
    (when (not (.isDirectory (io/file (str path "/out"))))
      (.mkdir (io/file (str path "/out"))))
    (generate-index path)
    (copy-css path)
    (generate-wiki path)
    (when (:watch options)
      (start-watch [{:path src-path
                     :event-types [:create :modify :delete]
                     :bootstrap (fn [path] (println "[OK] Starting to watch " path))
                     :callback (fn [event filename]
                                 (println "[OK] Changes detected " event filename)
                                 (generate-wiki path))
                     :options {:recursive true}}]))))
