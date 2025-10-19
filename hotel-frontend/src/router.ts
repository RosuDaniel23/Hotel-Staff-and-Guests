import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from './views/Dashboard.vue'
import Rooms from './views/Rooms.vue'
import Employees from './views/Employees.vue'
import Guests from './views/Guests.vue'

export default createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', name: 'dashboard', component: Dashboard },
        { path: '/rooms', name: 'rooms', component: Rooms },
        { path: '/employees', name: 'employees', component: Employees },
        { path: '/guests', name: 'guests', component: Guests },
    ]
})
