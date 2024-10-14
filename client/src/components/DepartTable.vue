<script setup lang="ts">
import { Ref, ref } from 'vue';
import request from '../utils/request';
import DepartDialog from './dialogs/DepartDialog.vue';
import { useAlertStore } from '../utils/store';

export interface Depart {
  id: number;
  name: string;
}

const pageSize: Ref<number> = ref(10);
const headers: { title: string, key: string | undefined, align: 'center' | 'start' | 'end' | undefined, sortable: boolean, value: string | undefined }[] = [
  { title: 'ID', key: 'id', value: undefined, align: "center", sortable: false },
  { title: '名称', key: undefined, value: 'name', align: "center", sortable: false },
  { title: '操作', key: undefined, value: 'actions', align: "center", sortable: false },
]
const items: Ref<Depart[]> = ref([]);
const totalItems: Ref<number> = ref(0);
const loading: Ref<boolean> = ref(false);

function loadItems({ page, pageSize, sortBy }: { page: number, pageSize: number, sortBy: string }) {
  sortBy;
  loading.value = true;
  request.get('depart', { params: { page, pageSize } }).then((response) => {
    items.value = response.data.data.list;
    totalItems.value = response.data.data.total;
  }).catch((error) => {
    console.error(error);
  }).finally(() => {
    loading.value = false;
  });
}

function deleteItem(item: Depart) {
  console.log(item);
  request.delete(`depart`, { params: { 'id': item.id } }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '删除成功');
  }).catch((error) => {
    console.error(error);
  });
}

function addItem(item: Depart) {
  console.log(item);
  request.put('depart', { 'name': item.name }).then(() => {
    items.value = [];
    loadItems({ page: 1, pageSize: pageSize.value, sortBy: '' });
    useAlertStore().showMessage('success', '添加成功');
  }).catch((error) => {
    console.error(error);
  });
}

function updateItem(item: Depart) {
  console.log(item);
  request.post(`depart/update`, item).then(() => {
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
        <DepartDialog @success="addItem" :init="null">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props" width="80px" prepend-icon="mdi-plus">添加</v-btn>
          </template>"
        </DepartDialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <DepartDialog @success="updateItem" :init="{ id: item.id, name: item.name }">
        <template v-slot:activator="{ props }">
          <v-icon size="small" v-bind="props">mdi-pencil</v-icon>
        </template>
      </DepartDialog>
      <v-icon size="small" @click="deleteItem(item)">mdi-delete</v-icon>
    </template>
  </v-data-table-server>
</template>