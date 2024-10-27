<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { EmploymentFull } from './EmploymentTable.vue';
import request from '../utils/request';
import EmploymentFullTable from './EmploymentFullTable.vue';


const props = defineProps<{ id: string }>();
const employment = ref<EmploymentFull | undefined>(undefined);
const isLoading = ref<boolean>(true);
const notFound = ref<boolean>(false);

async function getData() {
    console.log(`getting employment full info for ${props.id}`);
    isLoading.value = true;
    try {
        let response = await request.get('employment', { params: { id: Number.parseInt(props.id) } });
        employment.value = response.data.data;
    } catch (error: any) {
        if (error.response.status === 404) {
            notFound.value = true;
        } else {
            console.error(error);
        }
    } finally {
        isLoading.value = false;
    }
}

onMounted(() => {
    getData();
})
</script>

<template>
    <h1 v-if="!isLoading && notFound" class="h-center align-center">二维码已失效</h1>
    <EmploymentFullTable v-else :employment="employment" :isLoading="isLoading" :show-actions="false" width="100%" max-width="800"></EmploymentFullTable>
</template>