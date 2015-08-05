(ns markiki.handlers
  (:require
   [markiki.db :refer [default-value]]
   [re-frame.core :refer [register-handler dispatch]]
   [cognitect.transit :as t]
   [clojure.walk :as w]
   [ajax.core :refer [GET POST]]))



;; -- Helpers -----------------------------------------------------------------

(defn article-in-list
  "Checks if an article path is in our wiki.
  TODO: optimize. Now is in linear time."
  [articles path]
  (->> articles
       (map #(second (find % :path)))
       (some #(when (= % path) path))))


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
         articles (t/read r response)]
     (-> db
         (assoc :loading? false) ;; take away that modal
         (assoc :articles (into [] (map w/keywordize-keys articles)))))))


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
     :last-article (if (article-in-list (:articles db) path) path "")
     :searchbar "")))
