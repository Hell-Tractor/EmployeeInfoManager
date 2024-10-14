<script setup lang="ts">
import { onMounted, Ref, ref } from 'vue';
import { rules } from '../../utils/validators';
import { Depart } from '../DepartTable.vue';
import request from '../../utils/request';

const emits = defineEmits<{ success: [{ username: string, password: string, departId: number }] }>();

const username: Ref<string> = ref('');
const password: Ref<string> = ref('');
const comfirmPassword: Ref<string> = ref('');
const departId: Ref<number> = ref(0);
const isValid: Ref<boolean> = ref(false);

function reset() {
    username.value = '';
    password.value = '';
    departId.value = 0;
}

const allDeparts = ref<Depart[]>([]);

onMounted(() => {
    request.get('depart', { params: { page: 1, pageSize: 100 } }).then((response) => {
        allDeparts.value = response.data.data.list;
    }).catch((error) => {
        console.error(error);
    });
})
</script>

<template>
    <v-dialog width="400px">
        <template v-slot:activator="{ props }">
            <slot name="activator" :props="props"></slot>
        </template>
        <template v-slot:default="{ isActive }">
            <v-form v-model="isValid">
                <v-card>
                    <v-card-text>
                        <v-text-field v-model="username" label="用户名" :rules="[rules.required, rules.username]"></v-text-field>
                        <v-text-field v-model="password" label="密码" :rules="[rules.required, rules.password]" type="password"></v-text-field>
                        <v-text-field v-model="comfirmPassword" label="确认密码" :rules="[rules.required, rules.password]" type="password"></v-text-field>
                        <v-select v-model="departId" :items="allDeparts" item-title="name" item-value="id" label="公司" :rules="[rules.required]"></v-select>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn @click="isActive.value = false">取消</v-btn>
                        <v-btn @click="isActive.value = false; emits('success', { username, password, departId }); reset()" :disabled="isValid != true || password != comfirmPassword">保存</v-btn>
                    </v-card-actions>
                </v-card>
            </v-form>
        </template>
    </v-dialog>
</template>