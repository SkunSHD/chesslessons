(ns chesslessons.components.profile.components
	(:require
;       Models
		[chesslessons.visitor-model :refer [visitor, logout]]
		))

(def log (.-log js/console))
(defn cljs [string] (js->clj (.parse js/JSON string) :keywordize-keys true))



(defn render []
	[:div
	  [:div
	   [:a {:href (:link @visitor)}
            [:img { :width 150 :height 150 :src (:photo @visitor)}]
            [:h3 "User name:" (:name @visitor)]
	    ]]
        [:h6 "Email: " (:email@visitor)]
        [:h6 "Gender: " (:gender @visitor)]
	 ])