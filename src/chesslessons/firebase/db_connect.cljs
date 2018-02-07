(ns chesslessons.firebase.db-connect
	)

(def log (.-log js/console))

(def db (.database js/firebase))

(defn some-foo [str-obj]
	(log db))