import { defineStore } from "pinia";

export const useAlertStore = defineStore('alert', {
    state: () => ({
        type: 'success',
        message: '',
        show: false,
        timer: 0
    }),
    actions: {
        showMessage(type: string, message: string, duration: number = 3000) {
            if (this.show)
                clearTimeout(this.timer);
            this.type = type;
            this.message = message;
            this.show = true;
            this.timer = setTimeout(() => {
                this.show = false;
            }, duration);
        }
    }
})

export const useUserStore = defineStore('user', {
    state: () => ({
        token: '',
        username: '',
        level: ''
    }),
    actions: {
        set(token: string, username: string, level: string) {
            this.token = token;
            this.username = username;
            this.level = level;
        }
    }
})

export const useAppStore = defineStore('app', {
    state: () => ({
        page: 'home'
    }),
    actions: {
        redirectTo(page: string) {
            this.page = page;
        }
    }
})