(ns chesslessons.user-model
	(:require
		[reagent.core :refer [atom cursor]]
))

(def log (.-log js/console))
(defn cljs [object] (js->clj object :keywordize-keys true))
(defn cljs_parse [string] (js->clj (.parse js/JSON string) :keywordize-keys true))



; ==================
; Atoms
(defonce user (atom nil))


; ==================
; Private
(defn- -normalize_user [new_user]
	(let [formatted_user {
		 :profile (cljs (aget new_user "additionalUserInfo" "profile"))
		 :credential (cljs_parse (.stringify js/JSON (aget new_user "credential")))
		 :fbs_user (cljs_parse (.stringify js/JSON (aget new_user "user")))
		 :isNewUser (aget new_user "additionalUserInfo" "isNewUser")
		 :providerId (aget new_user "additionalUserInfo" "providerId")
         }]
		(log new_user 42)
		formatted_user))



; ==================
; Public
(defn set_user [new_user]
	(reset! user (-normalize_user new_user)))