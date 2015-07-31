(ns markiki.handlers
  (:require
   [markiki.db :refer [default-value schema]]
   [re-frame.core :refer [register-handler path trim-v after dispatch]]
   [cognitect.transit :as t]
   [ajax.core :refer [GET POST]]))

;; -- Handlers ----------------------------------------------------------------
;;
;; Handlers must return a db


(register-handler
 :initialize-db
 (fn [_ _]
   ;; Kick off the GET, making sure to supply a callback for success and failure
   (ajax.core/GET
    "markiki.json"
    {:handler       #(dispatch [:process-response %1]) ;; Further dispatch !!
     :error-handler #(dispatch [:bad-response %1])})
   default-value))


(register-handler
 :process-response
 (fn [db [_ response]]
   (let [r (t/reader :json)
         articles (t/read r response)]
     ;(println articles)
     (-> db
         (assoc :loading? false) ;; take away that modal
         (assoc :articles articles)))))


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
