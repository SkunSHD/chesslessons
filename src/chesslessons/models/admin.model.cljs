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

(defn- -admin-uid [new_admin]
	(aget new_admin "uid"))

(defn- -admin-email [new_admin]
	(aget new_admin "email"))


; TODO: Fix me
(defn- -normalize_admin [new_admin]
	(let [formatted_admin {
							 :uid (-admin-uid new_admin)
							 :email (-admin-email new_admin)
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
			(fn [new_admin] (set_admin (-normalize_admin new_admin)) (log 42 @admin)))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))
