<script setup lang="ts">
import { computed, onMounted, Ref, ref } from 'vue';
import request from '../utils/request';
import { useAlertStore, useUserStore } from '../utils/store';
import EmploymentDialog from './dialogs/EmploymentDialog.vue';
import { Staff } from './StaffPage.vue';
import { Depart } from './DepartTable.vue';
import EmploymentFullTable from './EmploymentFullTable.vue';
import { RiskTag } from './RiskTagTable.vue';
import jsPDF from 'jspdf';
import { CLIENT, QRCODE_SIZE } from '../config';
import QRCode from 'qrcode';
import pinyin from 'pinyin';

interface SimpleEmployment {
  id: number;
  staffId: number;
  staffName: string;
  project: string;
  validSince: Date;
  validUntil: Date;
}

export interface EmploymentFull {
  id: number;
  staff: Staff;
  depart: Depart;
  project: string;
  validSince: Date;
  validUntil: Date;
  riskTags: RiskTag[];
  workPermit: string;
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
const selected: Ref<number[]> = ref([]);

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
  request.delete(`employment`, { params: { 'id': item.id } }).then(() => {
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

const isLoadingFullInfo: Ref<boolean> = ref(false);
const employmentFullInfo: Ref<EmploymentFull | undefined> = ref(undefined);
async function getEmploymentFullInfo(id: number) {
  // if (id == employmentFullInfo.value?.id) {
  //   return;
  // }
  console.log(`getting employment full info for ${id}`);
  isLoadingFullInfo.value = true;
  try {
    let response = await request.get(`employment`, { params: { id } });
    employmentFullInfo.value = response.data.data;
    console.log(employmentFullInfo.value);
  } catch (error) {
    console.error(error);
  } finally {
    isLoadingFullInfo.value = false;
  }
}

const exporting: Ref<boolean> = ref(false);

async function exportQRCode() {
  exporting.value = true;
  const pdf = new jsPDF({ unit: "px", format: [QRCODE_SIZE, QRCODE_SIZE] });
  const canvasPromise = selected.value.map(async (id) => {
    const data = `${CLIENT}employment/${id}`;
    const canvas = document.createElement('canvas');
    await QRCode.toCanvas(canvas, data);
    return [canvas.toDataURL('image/png'), items.value.find(item => item.id === id)!.staffName];
  })

  const images = await Promise.all(canvasPromise);
  images.forEach((image, index) => {
    if (index > 0) {
      pdf.addPage([QRCODE_SIZE, QRCODE_SIZE]);
    }
    pdf.addImage(image[0], 'PNG', 0, 0, QRCODE_SIZE, QRCODE_SIZE);
    // write staff name at the bottom
    pdf.setFontSize(12);
    const pinyinName = pinyin(image[1], { style: pinyin.STYLE_NORMAL }).flat().join(' ');
    pdf.text(pinyinName, QRCODE_SIZE / 2, QRCODE_SIZE - 10, { align: 'center' });
  });

  pdf.save('qrcodes.pdf');
  exporting.value = false;
}

const allRiskTags = ref<RiskTag[]>([]);

onMounted(() => {
    request.get('risk_tag', { params: { page: 1, pageSize: 100 }}).then((response) => {
        allRiskTags.value = response.data.data.list;
    }).catch((error) => {
        console.error(error);
    });
})
</script>

<template>
  <v-data-table-server
    v-model:items-per-page="pageSize"
    :headers="headers"
    :items="items"
    :items-length="totalItems"
    :loading="loading"
    @update:options="loadItems"
    show-select
    v-model="selected"
  >
    <template v-slot:top>
      <v-toolbar>
        <v-btn color="primary" @click="exportQRCode()" :loading="exporting">导出二维码</v-btn>
        <v-spacer></v-spacer>
        <EmploymentDialog @success="refresh()" :all-risk-tags="allRiskTags">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" v-bind="props" width="80px" prepend-icon="mdi-plus">添加</v-btn>
          </template>"
        </EmploymentDialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-dialog width="800">
        <template v-slot:activator="{ props }">
          <v-icon size="small" style="margin-right: 10px;" v-bind="props" @click="getEmploymentFullInfo(item.id)">mdi-account-details</v-icon>
        </template>
        <template v-slot:default="{ isActive }">
          <EmploymentFullTable :show-actions="true" :employment="employmentFullInfo" :isLoading="isLoadingFullInfo" :all-risk-tags="allRiskTags" v-model="isActive.value" width="750"/>
        </template>
      </v-dialog>
      <v-icon size="small" @click="deleteItem(item)">mdi-delete</v-icon>
    </template>
  </v-data-table-server>
</template>