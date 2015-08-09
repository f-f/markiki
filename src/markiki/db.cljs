(ns markiki.db)

;; -- Default app-db Value  ---------------------------------------------------
;;
;; When the application first starts, this will be the value put in app-db
;; Look in core.cljs for "(dispatch-sync [:initialise-db])"
;;

(def default-value
  {:articles      []
   :searchbar     ""
   :last-article  {}
   :articles-tree {}
   :loading?      true })
