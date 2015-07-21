(ns markiki.core
  (:require [clojure.tools.cli :refer [cli]]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure-watch.core :refer [start-watch]])
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

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn generate-wiki [path]
  (println path))

(defn -main [& args]
  (let [[options arguments summary] (cli args
                                         ["-h" "--help" "Print this help"
                                          :default false
                                          :flag true]
                                         ["-w" "--watch" "Watch the folder for changes"
                                          :default false
                                          :flag true])
        path (first arguments)]
    ;; Handle help and error conditions
    ;; The user should provide a valid directory
    (cond
     (:help options) (exit 0 (usage summary))
     (not path) (exit 1 (usage summary))
     (not (.isDirectory (io/file path))) (exit 1 invalid-path-error))
    ;; Start generating!
    (generate-wiki path)
    (when (:watch options)
      (start-watch [{:path path
                     :event-types [:create :modify :delete]
                     :bootstrap (fn [path] (println "[OK] Starting to watch " path))
                     :callback (fn [event filename]
                                 (println "[OK] Changes detected " event filename)
                                 (generate-wiki path)) ;; TODO: optimize and regenerate only the file
                     :options {:recursive true}}]))))
