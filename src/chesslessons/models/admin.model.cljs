(ns chesslessons.admin-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fbs]
))


(def log (.-log js/console))
(defn cljs [object] (js->clj object :keywordize-keys true))
(defn cljs_parse [string] (js->clj (.parse js/JSON string) :keywordize-keys true))


; ==================
; Atoms
(defonce admin (atom nil))
(defonce sign_in_error_msg (atom ""))


; ==================
; Private
(defn- -normalize_admin [new_admin]
	(let [formatted_admin {
		                     :profile (cljs (aget new_admin "additionalUserInfo" "profile"))
		                     :credential (cljs_parse (.stringify js/JSON (aget new_admin "credential")))
		                     :fbs_user (cljs_parse (.stringify js/JSON (aget new_admin "user")))
		                     :isNewUser (aget new_admin "additionalUserInfo" "isNewUser")
		                     :providerId (aget new_admin "additionalUserInfo" "providerId")
		                     }]
		formatted_admin))


; ==================
; Public
(defn set_sign_in_error_msg [new_msg]
	(reset! sign_in_error_msg new_msg))

(defn set_admin [new_admin]
	(reset! admin new_admin))


(defn sign_in_admin [email password]
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [admin] (set_admin {:fbs_user (cljs_parse (.stringify js/JSON admin))})))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))


(defn admin_facebook_auth []
	(.catch (.then  (.signInWithPopup (fbs/auth)
	        fbs/facebook_auth_provider) (fn [admin] (set_admin (-normalize_admin admin))))
	        (fn [e] (log "B ERROR!" e))
	        ))
