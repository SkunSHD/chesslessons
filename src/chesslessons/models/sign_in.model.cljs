(ns chesslessons.sign_in-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fbs]
		))


; ==================
; Atoms
(defonce is_button_visible (atom false))


; ==================
; Public
(defn toggle [e]
	(.preventDefault e)
	(reset! is_button_visible (not @is_button_visible))
	)
