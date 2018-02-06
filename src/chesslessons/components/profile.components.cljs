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
	   [:a {:href (:link (:profile @user_model/user))}
	    [:img {:src (:url (:data (:picture (:profile @user_model/user))))}]
	    [:h3 "User name:" (:name (:profile @user_model/user))]
	    ]]
	   [:h6 "Email: " (:email (:profile @user_model/user))]
	   [:h6 "Gender: " (:gender (:profile @user_model/user))]
	 ])