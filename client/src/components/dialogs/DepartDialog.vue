<script setup lang="ts">
import { Ref, ref } from 'vue';
import { Depart } from '../DepartTable.vue';
import { rules } from '../../utils/validators';

const props = defineProps<{ init: Depart | null }>();
const emits = defineEmits<{ success: [Depart] }>();

const depart: Ref<Depart> = ref(props.init ?? { id: 0, name: '' });
const isValid: Ref<boolean> = ref(false);

function reset() {
    depart.value = { id: 0, name: '' };
}
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
                        <v-text-field v-model="depart.name" label="名称" :rules="[rules.required, rules.depart]"></v-text-field>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn @click="isActive.value = false">取消</v-btn>
                        <v-btn @click="isActive.value = false; emits('success', depart); reset()" :disabled="isValid != true">保存</v-btn>
                    </v-card-actions>
                </v-card>
            </v-form>
        </template>
    </v-dialog>
</template>