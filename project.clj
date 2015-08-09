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
                 [me.raynes/fs "1.4.6"]
                 [org.clojure/clojurescript "1.7.48"]
                 [re-frame "0.4.1"]
                 [prismatic/schema "0.4.3"]
                 [cljs-ajax "0.3.14"]
                 [secretary "1.2.3"]
                 [com.cognitect/transit-cljs "0.8.220"]
                 [clj-fuzzy "0.1.8"]
                 [markdown-clj "0.9.67"]
                 [reagent-forms "0.5.5"]
                 [reagent-utils "0.1.5"]]
  :main ^:skip-aot markiki.core
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :node-dependencies [[source-map-support "0.3.2"]]
  :plugins [[lein-npm "0.5.0"]]
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target/%s"
  :resource-paths ["resources"] ; non-code files included in classpath/jar
  :profiles {:uberjar {:aot :all}})
