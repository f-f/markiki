(require '[cljs.build.api :as b])

(println "[OK] Building Clojurescript")

(let [start (System/nanoTime)]
  (b/build "src"
           {:main 'markiki.core
            :output-to "resources/webres/markiki.js"
            :optimizations :advanced
            :verbose true})
  (println "[OK] Clojurescript done. Elapsed"
           (/ (- (System/nanoTime) start) 1e9) "seconds"))

(System/exit 0)
