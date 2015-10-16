(ns markiki.db)

;; -- Default app-db Value  ---------------------------------------------------
;;
;; When the application first starts, this will be the value put in app-db
;; Look in core.cljs for "(dispatch-sync [:initialise-db])"
;;

(def default-value
  {:articles          []       ;; Vector containing all the articles.
                               ;; Each article has the following properties:
                               ;; :title :text :last-modified :path

   :search-results    []       ;; Vector containing the 10 articles that better
                               ;; matched the last search query, ordered by score (DESC)

   :last-article      {}       ;; A single article - the last loaded

   :articles-tree     {}       ;; A map tree containing all the articles, in which
                               ;; a category is a new level of maps. It gets parsed
                               ;; to build the home index

   :title             ""       ;; The title of the wiki

   :description       ""       ;; Optional description for the homepage

   :loading?          true })  ;; If it's loading display the spinning cog
