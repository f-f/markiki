(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'markiki.core
          :output-to "resources/webres/markiki.js"
          :optimizations :advanced
          :verbose true})
