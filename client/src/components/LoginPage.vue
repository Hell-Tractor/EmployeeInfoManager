<script setup lang="ts">
import { ref, Ref } from "vue";
import request from "../utils/request";
import CryptoJS from "crypto-js";
import { useAlertStore, useAppStore, useUserStore } from "../utils/store";
import { rules } from "../utils/validators";

const username: Ref<string> = ref("");
const password: Ref<string> = ref("");
const isLoading: Ref<boolean> = ref(false);
const isValid: Ref<boolean> = ref(false);

async function login() {
    isLoading.value = true;
    try {
        let result = await request.get("user/login", { params: { 'username': username.value } });
        let salt = result.data.data;

        let encryptedPassword = CryptoJS.MD5(password.value + salt).toString();
        result = await request.post("user/login", { 'username': username.value, 'password': encryptedPassword, 'salt': salt });
        console.log(result.data);

        localStorage.setItem('token', result.data.data.token);
        localStorage.setItem('username', username.value);
        localStorage.setItem('level', result.data.data.level);
        localStorage.setItem('departId', result.data.data.departId.toString());

        const userStore = useUserStore();
        userStore.set(result.data.data.token, username.value, result.data.data.level, result.data.data.departId);

        const alertStore = useAlertStore();
        alertStore.showMessage("success", `登录成功, 欢迎您: ${username.value}`, 1000);

        const appStore = useAppStore();
        appStore.redirectTo('home');
    } catch (error) {
        console.error(error);
    } finally {
        isLoading.value = false;
    }
}
</script>

<template>
    <v-card title="登录" id="card" class="h-center">
        <v-card-text>
            <v-form v-model="isValid">
                <v-text-field v-model="username" label="用户名" :rules="[rules.required, rules.username]"></v-text-field>
                <v-text-field v-model="password" label="密码" type="password" :rules="[rules.required, rules.password]"></v-text-field>
                <v-btn @click="login" color="primary" :loading="isLoading" :disabled="isValid != true" width="100%">登录</v-btn>
            </v-form>
        </v-card-text>
    </v-card>
</template>

<style scoped>
#card {
    width: 400px;
    margin-top: 200px;
}
</style>