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


(defn markiki-app []
  (let [loading?      (subscribe [:loading?])
        searchbar-val (subscribe [:searchbar])
        last-article  (subscribe [:last-article])
        articles      (subscribe [:articles])]
    (fn []
      [:div
       [searchbar]
       [:div "Searching for: " @searchbar-val]])))

