(defproject markiki "0.1.0"
  :description "Static Personal Markdown Wiki"
  :url "https://github.com/ff-/markiki"
  :license {:name "The MIT License"
            :url "https://github.com/ff-/markiki/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [clojure-watch "0.1.11"]
                 [cheshire "5.5.0"]
                 [hiccup "1.0.5"]
                 [me.raynes/fs "1.4.6"]
                 [cpath-clj "0.1.2"]
                 [org.clojure/clojurescript "1.7.48" :classifier "aot"
                  :exclusion [org.clojure/data.json]]
                 [org.clojure/data.json "0.2.6" :classifier "aot"]
                 [re-frame "0.4.1"]
                 [prismatic/schema "1.0.1"]
                 [cljs-ajax "0.3.14"]
                 [secretary "1.2.3"]
                 [com.cognitect/transit-cljs "0.8.225"]
                 [clj-fuzzy "0.3.1"]
                 [markdown-clj "0.9.70"]
                 [com.andrewmcveigh/cljs-time "0.3.13"]]
  :main ^:skip-aot markiki.core
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :node-dependencies [[source-map-support "0.3.2"]]
  :plugins [[lein-npm "0.6.1"]
            [lein-bin "0.3.5"]]
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target/"
  :bin {:name "markiki"}
  :resource-paths ["resources"] ; non-code files included in classpath/jar
  :profiles {:uberjar {:aot :all}})
