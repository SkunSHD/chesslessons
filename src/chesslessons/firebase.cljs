(ns chesslessons.firebase
	(:require
		[reagent.core :refer [atom cursor]]
))

(def log (.-log js/console))

(def firebase js/firebase)


(defn auth []
	((.-auth firebase)))


(defn sign_in_with_email_and_password [email password]
	(.catch (.then
		(.signInWithEmailAndPassword (auth) email password) (fn [user] (log user "user...")))
	        (fn [e] (log (.message e)))))

