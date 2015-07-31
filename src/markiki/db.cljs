(ns markiki.db
  (:require [schema.core  :as s :include-macros true]))


;; -- Schema -----------------------------------------------------------------
;;
;; This is a Prismatic Schema which documents the structure of app-db
;; See: https://github.com/Prismatic/schema
;;
;; The value in app-db should ALWAYS match this schema. Now, the value in
;; app-db can ONLY be changed by event handlers so, after each event handler
;; has run, we re-check that app-db still matches this schema.
;;
;; How is this done? Look in handlers.cljs and you'll notice that all handers
;; have an "after" middleware which does the schema re-check.

(def schema {;; a sorted-map is used to hold the articles.
             :articles (s/both PersistentTreeMap ;; ensure sorted-map, not just map
                               ;; each article is keyed by its :path value
                               {s/Str {:path s/Str
                                       :title s/Str
                                       :text s/Str
                                       :last-modified s/Int}})
             :searchbar s/Str
             :last-article s/Str
             :loading? s/Bool })

;; -- Default app-db Value  ---------------------------------------------------
;;
;; When the application first starts, this will be the value put in app-db
;; Look in core.cljs for "(dispatch-sync [:initialise-db])"
;;

(def default-value            ;; what gets put into app-db by default.
  {:articles (sorted-map)     ;; an empty list of articles. Use the (str) :path as the key
   :searchbar ""
   :last-article ""
   :loading? true })          ;; show the homepage
