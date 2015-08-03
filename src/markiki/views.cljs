(ns markiki.views
  (:require [reagent.core  :as reagent :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]))


(defn searchbar []
  (fn []
    [:form
     [:div.form-group.has-error.has-feedback
      [:input#searchbar.form-control.input-lg
       {:type "text"
        :placeholder "Search"
        :on-change #(dispatch [:searchbar-change (-> % .-target .-value)])}]
      [:span.glyphicon.glyphicon-search.form-control-feedback]]]))

(defn article-link []
  (fn [{:keys [path title text last-modified]}]
    [:li [:a {:href (str "#" path)
              :on-click #(dispatch [:view-article path])} title]]))

(defn articles-list [articles]
  [:ul#articles-list
   (for [article @articles]
     [article-link article])])

(defn markiki-app []
  (let [loading?      (subscribe [:loading?])
        searchbar-val (subscribe [:searchbar])
        last-article  (subscribe [:last-article])
        articles      (subscribe [:articles])]
    (fn []
      (if @loading?
        [:i {:class "fa fa-cog fa-spin fa-5x"
             :style {:margin-top "3em"}}]
        [:div
         [searchbar]
         (cond
           (not (clojure.string/blank? @searchbar-val))
           [:div "Searching " @searchbar-val]
           (not (clojure.string/blank? @last-article))
           [:div "last-article: " @last-article]
           :else (if (empty? @articles)
                   [:h2 "No articles!"]
                   [articles-list articles]))]))))
