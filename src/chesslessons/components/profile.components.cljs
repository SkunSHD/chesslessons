(ns chesslessons.components.profile.components
	(:require
;       Models
		[chesslessons.visitor-model :refer [visitor, logout]]
;       Utils
		[goog.string :as gstring]
		[clojure.string :as str]
		))

(def log (.-log js/console))
(defn cljs [string] (js->clj (.parse js/JSON string) :keywordize-keys true))


(defn visitor_name []
	(first (str/split (:name @visitor) #"\s" )))


(defn render []
	[:div
	  [:div.alert.alert-warning.alert-dismissible.fade.show {:role "alert"}
	   "Hi, " [:strong (visitor_name)] "! I'm gonna send you a massage or something in closest future!"
	   [:button.close {:type "button" :data-dismiss "alert" :aria-label "Close"}
	   	[:span {:aria-hidden "true"} (gstring/unescapeEntities "&times;")]]]

	   [:a {:href (:link @visitor)}
			 [:img { :width 150 :height 150 :src (:photo @visitor)}]
			 [:h3 "User name:" (:name @visitor)]]
		 [:h6 "Email: " (:email@visitor)]
	 ])