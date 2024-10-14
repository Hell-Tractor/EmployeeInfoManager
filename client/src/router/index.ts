import { createRouter, createWebHistory } from "vue-router";
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
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;