(defproject markiki "0.1.0"
  :description "Static Personal Markdown Wiki"
  :url "https://github.com/ff-/markiki"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [clojure-watch "0.1.11"]
                 [cheshire "5.5.0"]]
  :main ^:skip-aot markiki.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
