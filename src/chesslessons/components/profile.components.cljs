(ns chesslessons.components.profile.components
	(:require
;       Models
		[chesslessons.user-model :as user_model]
		))

(def log (.-log js/console))
(defn cljs [string] (js->clj (.parse js/JSON string) :keywordize-keys true))



(defn render []
	[:div
	  [:div
	   [:a {:href (:link @user_model/user)}
	    [:img {:src (:photo @user_model/user)}]
	    [:h3 "User name:" (:name @user_model/user)]
	    ]]
	   [:h6 "Email: " (:email@user_model/user)]
	   [:h6 "Gender: " (:gender @user_model/user)]
	 ])