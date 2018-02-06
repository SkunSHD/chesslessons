(ns chesslessons.user-model
	(:require
		[reagent.core :refer [atom cursor]]
))

(defn cljs [object] (js->clj object :keywordize-keys true))

(defonce user (atom nil))



(defn set_user [new_user]
	(reset! user (cljs new_user)))