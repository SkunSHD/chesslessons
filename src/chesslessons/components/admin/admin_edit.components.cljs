(ns chesslessons.components.admin_edit.components
	(:require
;		Components
		[chesslessons.components.admin.admin-edit-form.components :as admin_form_components]
))


(def log (.-log js/console))


(defn render_edit_admin_container []
	 [:div
	   	[:h1 "admin EDIT container"]
	  	[admin_form_components/render_admin_form]
	  ])