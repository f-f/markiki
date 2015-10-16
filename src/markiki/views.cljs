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
      [:input#searchbar.form-control
       {:type "text"
        :placeholder "Search"
        :on-change #(dispatch [:searchbar-change (-> % .-target .-value)])}]
      [:span.glyphicon.glyphicon-search.form-control-feedback]]]))


(defn nav []
  [:nav.navbar.navbar-inverse.navbar-fixed-top
   [:div.container
    [:div.navbar-header
     [:button.navbar-toggle.collapsed
      {:type "button"
       :data-toggle "collapse"
       :data-target "#navbar"
       :aria-expanded "false"
       :aria-controls "navbar"}
      [:span.sr-only "Toggle navigation"]
      [:span.icon-bar]
      [:span.icon-bar]
      [:span.icon-bar]]
     [:a.navbar-brand {:href "#"} "Markiki"]]
    [:div#navbar.collapse.navbar-collapse
     [:ul.nav.navbar-nav.navbar-right
      [:li [searchbar]]]]]])


(defn display-articles-list [articles]
  [:ul.articles-list
   (for [a articles
         :let [k (first a)
               v (second a)]]
     (if (contains? v :path)
       [:li [:a {:href (str "#" (:path v))} (:title v)]]
       [:li
        [:h4 k]
        [display-articles-list v]]))])


(defn display-searchlist [results]
  [:div.list-group
   (for [a results]
     [:a.list-group-item {:href (str "#" (:path a))}
      [:span.badge [:h6 (.toFixed (:score a) 2)]]
      [:h4 (:title a)]])])


(defn display-article []
  (fn [{:keys [path title text last-modified]}]
    (let [parsed (str (md->html text))]
      [:div.article.row
       [:h1.title title]
       [:h4.last-edit "Last modified: " (f/unparse
                                         (f/formatter "EEEE dd MMM yyyy HH:mm:ss")
                                         (c/from-long last-modified))]
       [:div.col-md-8.col-md-offset-2 {:dangerouslySetInnerHTML {:__html parsed}}]])))


(defn markiki-app []
  (let [loading?      (subscribe [:loading?])
        searchlist    (subscribe [:search-results])
        last-article  (subscribe [:last-article])
        articles      (subscribe [:articles])
        articles-tree (subscribe [:articles-tree])]
    (fn []
      (if @loading?
        [:i.fa.fa-cog.fa-spin.fa-5x]
        [:div
         [nav]
         [:div#loaded.container
          (cond
           (not (empty? @searchlist))    [display-searchlist @searchlist]
           (not (empty? @last-article))  [display-article @last-article]
           (not (empty? @articles))      [display-articles-list @articles-tree]
           :else                         [:h1 "No articles!"])]]))))

