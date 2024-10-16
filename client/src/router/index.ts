import { createRouter, createWebHashHistory } from "vue-router";
import EmploymentRead from "../components/EmploymentRead.vue";
import Home from "../components/Home.vue";

const routes = [
    {
        path: '/employment/:id',
        component: EmploymentRead,
        props: true
    },
    {
        path: '/',
        component: Home
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router;