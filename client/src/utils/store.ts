import { defineStore } from "pinia";

export const useAlertStore = defineStore('alert', {
    state: (): { type: string, message: string, show: boolean, timer: NodeJS.Timeout | null } => ({
        type: 'success',
        message: '',
        show: false,
        timer: null
    }),
    actions: {
        showMessage(type: string, message: string, duration: number = 3000) {
            if (this.timer)
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
        level: '',
        departId: -1,
    }),
    actions: {
        set(token: string, username: string, level: string, departId: number) {
            this.token = token;
            this.username = username;
            this.level = level;
            this.departId = departId;
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