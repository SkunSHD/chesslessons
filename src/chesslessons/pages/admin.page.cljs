(ns chesslessons.admin.page
	(:require
;		Models
		[chesslessons.admin-model :as admin_model]
;		Components
		[chesslessons.components.admin.components :as admin_components]
		[chesslessons.components.admin-visitors.components :as admin_visitors]
))

(def log (.-log js/console))


; ==================
; Public
(defn render []
	[:div
	    (if (nil? @admin_model/admin)
		  [admin_components/render_login_form]
		  [admin_visitors/render]
;		  [admin_components/render_admin_container]
			)
	 ])

