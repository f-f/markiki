(defproject markiki "0.1.0"
  :description "Static Personal Markdown Wiki"
  :url "https://github.com/ff-/markiki"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [clojure-watch "0.1.11"]
                 [cheshire "5.5.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/clojurescript "0.0-3308" :classifier "aot"
                  :exclusion [org.clojure/data.json]]
                 [org.clojure/data.json "0.2.6" :classifier "aot"]]
  :main ^:skip-aot markiki.core
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :node-dependencies [[source-map-support "0.3.2"]]
  :plugins [[lein-npm "0.5.0"]]
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target/%s"
  :resource-paths ["resources"] ; non-code files included in classpath/jar
  :profiles {:uberjar {:aot :all}})
