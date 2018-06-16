(ns status-im.chat.views.api.choose-asset
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.colors :as colors]
            [status-im.utils.money :as money]))

(defn- render-asset [arg-index bot-db-key]
  (fn [{:keys [name symbol amount decimals] :as asset}]
    [react/touchable-highlight {:on-press #(re-frame/dispatch
                                            [:set-asset-as-command-argument {:arg-index  arg-index
                                                                             :bot-db-key bot-db-key
                                                                             :asset      (dissoc asset :amount)}])}
     [react/view {:flex-direction   :row
                  :align-items      :center
                  :justify-content  :space-between
                  :padding-vertical 11}
      [react/view {:flex            1
                   :flex-direction  :row
                   :align-items     :center}
       [react/image {:source (-> asset :icon :source)
                     :style  {:width        30
                              :height       30
                              :margin-left  14
                              :margin-right 12}}]
       [react/text {:style {:color :black}} symbol]
       [react/text {:style {:color        :gray
                            :padding-left 4}} name]]
      [react/text {:style {:color         :gray
                           :padding-right 14}}
       (str (money/internal->formatted amount symbol decimals))]]]))

(def assets-separator [react/view {:height           1
                                   :background-color colors/gray-light
                                   :margin-left      56}])

(defview choose-asset-view [{arg-index  :index
                             bot-db-key :bot-db-key}]
  (letsubs [assets [:wallet/visible-assets-with-amount]]
    [react/view
     [list/flat-list {:data                      assets
                      :key-fn                    (comp name :symbol)
                      :render-fn                 (render-asset arg-index bot-db-key)
                      :enableEmptySections       true
                      :separator                 assets-separator
                      :keyboardShouldPersistTaps :always
                      :bounces                   false}]]))