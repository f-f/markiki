(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'markiki.core
          :output-to "resources/js/markiki.js"
          :optimizations :advanced
          :verbose true})
