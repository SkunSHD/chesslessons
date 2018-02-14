(ns chesslessons.sign_in-model
	(:require
		[chesslessons.firebase :as fbs]
;		Utils
		[chesslessons.atom.utils  :refer [atom! action!]]
		))


; ==================
; Atoms
(defonce is_button_visible (atom! "[sign_in.model/is_button_visible]" false))


; ==================
; Actions
(defn toggle [e]
	(action! "[sign_in.model/toggle]")
	(.preventDefault e)
	(reset! is_button_visible (not @is_button_visible))
	)
