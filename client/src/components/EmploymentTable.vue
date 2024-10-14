<script setup lang="ts">
import { computed, Ref, ref } from 'vue';
import request from '../utils/request';
import { useAlertStore, useUserStore } from '../utils/store';
import EmploymentDialog from './dialogs/EmploymentDialog.vue';

interface SimpleEmployment {
  id: number;
  staffId: number;
  staffName: string;
  project: string;
  validSince: Date;
  validUntil: Date;
}

export interface Employment {
  id: number;
  staffId: number;
  departId: number;
  project: string;
  validSince: Date;
  validUntil: Date;
  workPermit: string;
  riskTagIds: number[];
  violation: string;
}

const pageSize: Ref<number> = ref(10);
const headers: { title: string, key: string | undefined, align: 'center' | 'start' | 'end' | undefined, sortable: boolean, value: string | ((item: Record<string, SimpleEmployment>) => string) | undefined }[] = [
  { title: 'ID', key: 'id', value: undefined, align: "center", sortable: false },
  { title: '员工姓名', key: undefined, value: 'staffName', align: "center", sortable: false },
  { title: '项目名称', key: undefined, value: 'project', align: "center", sortable: false },
  { title: '有效期', key: 'validTime', value: item => `${item.validSince.toLocaleString()} - ${item.validUntil.toLocaleString()}`, align: "center", sortable: false },
  { title: '操作', key: undefined, value: 'actions', align: "center", sortable: false },
]
const items: Ref<SimpleEmployment[]> = ref([]);
const totalItems: Ref<number> = ref(0);
const loading: Ref<boolean> = ref(false);
const departId = computed(() => useUserStore().departId);

function loadItems({ page, pageSize, sortBy }: { page: number, pageSize: number, sortBy: string }) {
  sortBy;
  loading.value = true;
  request.get(`employment/depart/${departId.value}/all`, { params: { page, pageSize } }).then((response) => {
    items.value = response.data.data.list;
    totalItems.value = response.data.data.total;
  }).catch((error) => {
    console.error(error);
  }).finally(() => {
    loading.value = false;
  });
}

function deleteItem(item: SimpleEmployment) {
  console.log(item);
  request.delete(`employment`, { params: { 'userId': item.id } }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '删除成功');
  }).catch((error) => {
    console.error(error);
  });
}

function refresh() {
  items.value = [];
  loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
}
</script>

<template>
  <v-data-table-server
    v-model:items-per-page="pageSize"
    :headers="headers"
    :items="items"
    :items-length="totalItems"
    :loading="loading"
    @update:options="loadItems"
  >
    <template v-slot:top>
      <v-toolbar>
        <v-spacer></v-spacer>
        <EmploymentDialog @success="refresh()" :init="null">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props" width="80px" prepend-icon="mdi-plus">添加</v-btn>
          </template>"
        </EmploymentDialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon size="small" @click="deleteItem(item)">mdi-delete</v-icon>
    </template>
  </v-data-table-server>
</template>