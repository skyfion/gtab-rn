(ns gpad.events
  (:require [re-frame.core :as rf]))

(defonce nav-ref-state (atom nil))

(rf/reg-event-fx
  :navigate-to
  (fn [{:keys [db]} [_ route]]
    {:db       db
     :navigate route}))

(rf/reg-fx
  :navigate
  (fn [route]
    (when-let [nav-ref @nav-ref-state]
      (.navigate ^js nav-ref route))))

(rf/reg-event-fx
  :show-song
  (fn [{:keys [db]} [_ id]]
    (let [lyric (:lyric (get (:data db) id))]
      {:db       (assoc db :lyric lyric)
       :navigate :song})))

(rf/reg-sub
  :lyric
  (fn [db _]
    (:lyric db)))

(rf/reg-event-db
  :init-data
  (fn [db [_ data]]
    (assoc db :data data)))

(rf/reg-sub
  :data
  (fn [db _]
    (or (:data db) [])))
