(ns markiki.handlers
  (:require
   [markiki.db :refer [default-value]]
   [re-frame.core :refer [register-handler dispatch]]
   [cognitect.transit :as t]
   [clojure.walk :as w]
   [clojure.string :as string]
   [ajax.core :refer [GET POST]]))



;; -- Helpers -----------------------------------------------------------------

(defn article-in-list
  "Checks if an article path is in our wiki.
  TODO: optimize. Now is in linear time."
  [articles path]
  (->> articles
       (map (fn [x] (if (= path (:path x)) x nil)))
       (some not-empty)))


(defn make-articles-tree
  "Takes a list of articles and builds in the state a tree for pretty print"
  [v]
  (let [t (atom {})]
    (doseq [a v]
      (let [path (-> (:path a) (string/split #"/") rest)]
        (reset! t (assoc-in @t
                            path
                            (select-keys a [:path :title])))))
    @t))


;; -- Handlers ----------------------------------------------------------------
;;
;; Handlers must return a db


(register-handler
 :initialize-db
 (fn [_ _]
   ;; Kick off the GET, making sure to supply a callback for success and failure
   (ajax.core/GET
    "markiki.json"
    {:handler       #(dispatch [:process-response %]) ;; Further dispatch !!
     :error-handler #(dispatch [:bad-response %])})
   default-value))


(register-handler
 :process-response
 (fn [db [_ response]]
   (let [r (t/reader :json)
         articles (into [] (map w/keywordize-keys (t/read r response)))]
     (-> db
         (assoc :loading? false) ;; take away that modal
         (assoc :articles articles)
         (assoc :articles-tree (make-articles-tree articles))))))


(register-handler
 :bad-response
 (fn [_ _]
   ;; Retries in 10 seconds
   (js/setTimeout (fn [] (dispatch [:initialize-db])) 10000)
   (assoc default-value :loading? true)))


(register-handler
 :searchbar-change
 (fn [db [_ searchstring]]
   (assoc db :searchbar searchstring)))


(register-handler
 :view-article
 (fn [db [_ path]]
   (assoc db
     :last-article (let [article (article-in-list (:articles db) path)]
                     (if article article ""))
     :searchbar "")))
