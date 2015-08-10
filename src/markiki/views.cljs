(ns markiki.views
  (:require [reagent.core  :as reagent :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [cljs-time.format :as f]
            [cljs-time.coerce :as c]
            [markdown.core :refer [md->html]]))


(defn searchbar []
  (fn []
    [:form
     [:div.form-group.has-error.has-feedback
      [:input#searchbar.form-control.input-lg
       {:type "text"
        :placeholder "Search"
        :on-change #(dispatch [:searchbar-change (-> % .-target .-value)])}]
      [:span.glyphicon.glyphicon-search.form-control-feedback]]]))


(defn article-link [{:keys [path title]}]
  [:li [:a {:href (str "#" path)} title]])


(defn articles-list [articles]
  [:ul.articles-list
   (for [article articles
         :let [k (first article)
               v (second article)]]
     (if (contains? v :path)
       [article-link v]
       [:li
        [:h4 k]
        [articles-list v]]))])


(defn display-searchlist [results]
  [:div.list-group
   (for [a results]
     [:a.list-group-item {:href (str "#" (:path a))}
      [:span.badge [:h6 (.toFixed (:score a) 2)]]
      [:h4 (:title a)]])])


(defn display-article []
  (fn [{:keys [path title text last-modified]}]
    [:div
     [:h1 title]
     [:h4 "Last modified: " (f/unparse (f/formatter "EEEE dd MMM yyyy HH:mm:ss")
                                       (c/from-long last-modified))]
     [:div {:dangerouslySetInnerHTML {:__html (md->html text)}}]]))

(defn markiki-app []
  (let [loading?      (subscribe [:loading?])
        searchlist    (subscribe [:search-results])
        last-article  (subscribe [:last-article])
        articles      (subscribe [:articles])
        articles-tree (subscribe [:articles-tree])]
    (fn []
      (if @loading?
        [:i.fa.fa-cog.fa-spin.fa-5x]
        [:div#loaded
         [searchbar]
         (cond
          (not (empty? @searchlist))    [display-searchlist @searchlist] ;;[:div "Searching"]
          (not (empty? @last-article))  [display-article @last-article]
          (not (empty? @articles))      [articles-list @articles-tree]
          :else                         [:h1 "No articles!"])]))))
