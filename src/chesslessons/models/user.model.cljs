(ns chesslessons.user-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase.db :as db]
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
	(log "before " new_user)
	(let [formatted_user {
		 :profile (cljs (aget new_user "additionalUserInfo" "profile"))
		 :credential (cljs_parse (.stringify js/JSON (aget new_user "credential")))
		 :fbs_user (cljs_parse (.stringify js/JSON (aget new_user "user")))
		 :isNewUser (aget new_user "additionalUserInfo" "isNewUser")
		 :providerId (aget new_user "additionalUserInfo" "providerId")
         }]
		formatted_user))

(defn- -save_in_firebase [formatted_user]
	(db/save_user formatted_user))


; ==================
; Public
(defn set_user [new_user]
	(let [formatted_user (-normalize_user new_user)]
		((reset! user formatted_user)
			(-save_in_firebase  formatted_user)))
	)