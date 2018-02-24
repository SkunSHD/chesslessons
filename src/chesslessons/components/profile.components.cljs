(ns chesslessons.components.profile.components
	(:require
;       Models
		[chesslessons.visitor-model :as user_model]
		))

(def log (.-log js/console))
(defn cljs [string] (js->clj (.parse js/JSON string) :keywordize-keys true))



(defn render []
	[:div
	  [:div
	   [:a {:href (:link @user_model/visitor)}
	    [:img { :width 150 :height 150 :src (:photo @user_model/visitor)}]
	    [:h3 "User name:" (:name @user_model/visitor)]
	    ]]
	   [:h6 "Email: " (:email@user_model/visitor)]
	   [:h6 "Gender: " (:gender @user_model/visitor)]
	 ])