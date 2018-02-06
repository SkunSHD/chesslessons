(ns chesslessons.firebase
	(:require
		[reagent.core :refer [atom cursor]]
;		Models
		[chesslessons.user-model :as user_model]
))

(def log (.-log js/console))

(def firebase js/firebase)


(defn auth []
	(.auth firebase))


(defn sign_in_with_email_and_password [email password]
	(.catch (.then
		(.signInWithEmailAndPassword (auth) email password) (fn [user] (log user "user...")))
	        (fn [e] (log (.message e)))))


; ==================
; Facebook
(def facebook_auth_provider (new (.-FacebookAuthProvider (.-auth firebase))))


(defn facebook_link_user [user credential]
	(log user "HE2RE!!!" credential)
	(.then (.link user credential) (fn [wtf] (log "VSE" wtf))))


(defn facebook_fect_provider_for_email [email credential]
	(.then (.fetchProvidersForEmail (auth) email)
	       (fn [providers]
		       (.then (.signInWithEmailAndPassword (auth) email "ward121314")
		              (fn [user] (facebook_link_user user credential))))))


(defn facebook_auth_error [error]
	(log error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
		(facebook_fect_provider_for_email (.-email error) (.-credential error))))


(defn facebook_auth []
	(.catch (.then
		  (.signInWithPopup (auth) facebook_auth_provider) user_model/set_user)
	        facebook_auth_error))


