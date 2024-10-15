<script setup lang="ts">
import { computed, Ref, ref } from 'vue';
import request from '../utils/request';
import { useAlertStore, useUserStore } from '../utils/store';
import RiskTagDialog from './dialogs/RiskTagDialog.vue';

export interface RiskTag {
  id: number;
  name: string;
}

interface Header { title: string, key: string | undefined, align: 'center' | 'start' | 'end' | undefined, sortable: boolean, value: string | undefined };

const pageSize: Ref<number> = ref(10);
const basicHeaders: Header[] = [
  { title: 'ID', key: 'id', value: undefined, align: "center", sortable: false },
  { title: '名称', key: undefined, value: 'name', align: "center", sortable: false },
]
const rootHeaders: Header[] = [
  { title: '操作', key: undefined, value: 'actions', align: "center", sortable: false },
]
const headers = computed(() => {
  if (useUserStore().level === 'ROOT') {
    return [...basicHeaders, ...rootHeaders];
  } else {
    return basicHeaders;
  }
});
const items: Ref<RiskTag[]> = ref([]);
const totalItems: Ref<number> = ref(0);
const loading: Ref<boolean> = ref(false);

function loadItems({ page, pageSize, sortBy }: { page: number, pageSize: number, sortBy: string }) {
  sortBy;
  loading.value = true;
  request.get('risk_tag', { params: { page, pageSize } }).then((response) => {
    items.value = response.data.data.list;
    totalItems.value = response.data.data.total;
  }).catch((error) => {
    console.error(error);
  }).finally(() => {
    loading.value = false;
  });
}

function deleteItem(item: RiskTag) {
  console.log(item);
  request.delete(`risk_tag`, { params: { 'id': item.id } }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '删除成功');
  }).catch((error) => {
    console.error(error);
  });
}

function addItem(item: RiskTag) {
  console.log(item);
  request.put('risk_tag', { 'name': item.name }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '添加成功');
  }).catch((error) => {
    console.error(error);
  });
}

function updateItem(item: RiskTag) {
  console.log(item);
  request.post(`risk_tag/update`, item).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '更新成功');
  }).catch((error) => {
    console.error(error);
  });
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
        <RiskTagDialog @success="addItem" :init="null">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props" width="80px" prepend-icon="mdi-plus">添加</v-btn>
          </template>"
        </RiskTagDialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }" v-if="useUserStore().level === 'ROOT'">
      <RiskTagDialog @success="updateItem" :init="{ id: item.id, name: item.name }">
        <template v-slot:activator="{ props }">
          <v-icon size="small" v-bind="props">mdi-pencil</v-icon>
        </template>
      </RiskTagDialog>
      <v-icon size="small" @click="deleteItem(item)">mdi-delete</v-icon>
    </template>
  </v-data-table-server>
</template>