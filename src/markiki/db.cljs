(ns markiki.db)

;; -- Default app-db Value  ---------------------------------------------------
;;
;; When the application first starts, this will be the value put in app-db
;; Look in core.cljs for "(dispatch-sync [:initialise-db])"
;;

(def default-value            ;; what gets put into app-db by default.
  {:articles []     ;; an empty list of articles. Use the (str) :path as the key
   :searchbar ""
   :last-article {}
   :loading? true })          ;; show the homepage
