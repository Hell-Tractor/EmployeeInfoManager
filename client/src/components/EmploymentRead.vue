<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { RiskTag } from './RiskTagTable.vue';
import { EmploymentFull } from './EmploymentTable.vue';
import request from '../utils/request';
import EmploymentFullTable from './EmploymentFullTable.vue';


const props = defineProps<{ id: string }>();
const employment = ref<EmploymentFull | undefined>(undefined);
const isLoading = ref<boolean>(true);
const allRiskTags = ref<RiskTag[]>([]);

async function getData() {
    console.log(`getting employment full info for ${props.id}`);
    isLoading.value = true;
    let response = await request.get('risk_tag', { params: { page: 1, pageSize: 100 } });
    allRiskTags.value = response.data.data.list;
    response = await request.get('employment', { params: { id: Number.parseInt(props.id) } });
    employment.value = response.data.data;
    isLoading.value = false;
}

onMounted(() => {
    getData();
})
</script>

<template>
    <EmploymentFullTable :employment="employment" :isLoading="isLoading" :allRiskTags="allRiskTags" :show-actions="false"></EmploymentFullTable>
</template>