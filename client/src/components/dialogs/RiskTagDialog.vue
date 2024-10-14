<script setup lang="ts">
import { Ref, ref } from 'vue';
import { rules } from '../../utils/validators';
import { RiskTag } from '../RiskTagTable.vue';

const props = defineProps<{ init: RiskTag | null }>();
const emits = defineEmits<{ success: [RiskTag] }>();

const riskTag: Ref<RiskTag> = ref(props.init ?? { id: 0, name: '' });
const isValid: Ref<boolean> = ref(false);

function reset() {
    riskTag.value = { id: 0, name: '' };
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
                        <v-text-field v-model="riskTag.name" label="名称" :rules="[rules.required, rules.riskTag]"></v-text-field>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn @click="isActive.value = false">取消</v-btn>
                        <v-btn @click="isActive.value = false; emits('success', riskTag); reset()" :disabled="isValid != true">保存</v-btn>
                    </v-card-actions>
                </v-card>
            </v-form>
        </template>
    </v-dialog>
</template>