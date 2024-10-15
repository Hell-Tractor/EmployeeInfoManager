<script setup lang="ts">
import { computed, ComputedRef, ref, Ref } from 'vue';
import { SERVER } from '../config';
import { EmploymentFull } from './EmploymentTable.vue';
import { rules } from '../utils/validators';
import { RiskTag } from './RiskTagTable.vue';
import request from '../utils/request';
import { useAlertStore } from '../utils/store';

const props = defineProps<{ employment?: EmploymentFull, isLoading: boolean, allRiskTags: RiskTag[], showActions: boolean }>();
const isActive = defineModel<boolean>();

const isValid: Ref<boolean> = ref(false);
const staffImageURL = computed(() => `${SERVER}image/staff/${props.employment!.staff.image}`);
const newWorkPermitImageURL: Ref<string | null> = ref(null);
const workPermitImageURL = computed(() => newWorkPermitImageURL.value ?? `${SERVER}image/workPermit/${props.employment!.workPermit}`);
const workPermitImageFile = ref<File | null>(null);
const staffInTable = computed(() => props.employment!.staff);

const readonly: Ref<boolean> = ref(true);
const isSaving: Ref<boolean> = ref(false);
const oldRiskTags: Ref<number[]> = ref([]);

async function editStart() {
  oldRiskTags.value = props.employment!.riskTags.map(tag => tag.id);
  console.log('tags backuped: ' + oldRiskTags.value);
  readonly.value = false;
}

interface UpdateEmploymentVo {
  id: number;
  staffId: number;
  departId: number;
  project: string;
  validSince: Date;
  validUntil: Date;
  workPermit: string;
  violation: string;
}

async function deleteImage() {
  if (!!props.employment!.workPermit) {
    await request.delete(`image/workPermit/${props.employment!.workPermit}`);
    props.employment!.workPermit = '';
  } else {
    console.log('no image to delete');
  }
}

async function uploadImage() {
  if (workPermitImageFile.value === null) {
    return;
  }
  try {
    await deleteImage();

    let formData = new FormData();
    formData.append('file', workPermitImageFile.value!);
    let response = await request.put('image/upload/workPermit', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
    props.employment!.workPermit = response.data.data;
  } catch (error) {
    console.error(error);
  }
}

async function selectImage() {
  let file = await new Promise<File>((resolve) => {
    let input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.onchange = () => {
      if (input.files !== null) {
        resolve(input.files[0]);
      }
    };
    input.click();
  });
  newWorkPermitImageURL.value = URL.createObjectURL(file);
  workPermitImageFile.value = file;
}

async function updateItem() {
  isSaving.value = true;
  try {
    await uploadImage();
  } catch (error) {
    console.error(error);
    await deleteImage();
    isSaving.value = false;
    return;
  }
  try {
    for (let tag of props.employment!.riskTags) {
      let riskTagId = tag.id;
      if (!oldRiskTags.value.includes(riskTagId)) {
        console.log("add risk tag: " + riskTagId);
        await request.put(`employment/${props.employment!.id}/risk_tag`, null, { params: { 'riskTagId': riskTagId } });
      }
    }
    for (let tagId of oldRiskTags.value) {
      if (props.employment!.riskTags.find(tag => tag.id === tagId) === undefined) {
        console.log("delete risk tag: " + tagId);
        await request.delete(`employment/${props.employment!.id}/risk_tag`, { params: { 'riskTagId': tagId } });
      }
    }
    let vo: UpdateEmploymentVo = {
      id: props.employment!.id,
      staffId: props.employment!.staff.id,
      departId: props.employment!.depart.id,
      project: props.employment!.project,
      validSince: props.employment!.validSince,
      validUntil: props.employment!.validUntil,
      workPermit: props.employment!.workPermit,
      violation: props.employment!.violation,
    }

    await request.post(`employment/update`, vo);
    useAlertStore().showMessage('success', '保存成功');
  } catch (error) {
    console.error(error);
  } finally {
    isSaving.value = false;
    readonly.value = true;
  }
}
</script>

<template>
  <v-card>
    <v-card-text>
      <v-list-item v-if="isLoading" title="加载中..." width="175px" class="h-center">
        <template v-slot:append>
          <v-progress-circular color="primary" indeterminate="disable-shrink" size="20"></v-progress-circular>
        </template>
      </v-list-item>
      <v-form v-model="isValid" v-else>
        <table class="h-center" border="1" cellspacing="0">
          <tbody>
            <tr>
              <td>
                <v-text-field label="姓名" width="250px" density="compact" :readonly="true" v-model="staffInTable.name" :hide-details="true"></v-text-field>
              </td>
              <td>
                <v-text-field label="年龄" width="250px" density="compact" :readonly="true" v-model.number="staffInTable.age" :hide-details="true"></v-text-field>
              </td>
              <td rowspan="2">
                <v-img :src="staffImageURL" width="150px" height="225px"></v-img>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <v-text-field label="身份证号" width="500px" density="compact" :readonly="true" v-model="staffInTable.personId" :hide-details="true"></v-text-field>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="个人经历" width="650px" :readonly="true" v-model="staffInTable.experience" :hide-details="true"></v-textarea>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="身体状况" width="650px" :readonly="true" v-model="staffInTable.physicalCondition" :hide-details="true"></v-textarea>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="备注" width="650px" :readonly="true" v-model="staffInTable.appendix" :hide-details="true"></v-textarea>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-text-field label="项目名称" width="650px" density="compact" :rules="[rules.required, rules.projectName]" v-model="props.employment!.project" :readonly="readonly" :hide-details="readonly ? true : 'auto'"></v-text-field>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-row>
                  <v-col cols="6">
                    <v-text-field v-model="props.employment!.validSince" density="compact" label="有效期自" type="date" :rules="[rules.required]" :readonly="readonly" :hide-details="readonly ? true : 'auto'"></v-text-field>
                  </v-col>
                  <v-col cols="6">
                    <v-text-field v-model="props.employment!.validUntil" density="compact" label="有效期至" type="date" :rules="[rules.required]" :readonly="readonly" :hide-details="readonly ? true : 'auto'"></v-text-field>
                  </v-col>
                </v-row>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-select v-model="props.employment!.riskTags" item-title="name" return-object :items="allRiskTags" label="风险标签" multiple chips :readonly="readonly" :hide-details="readonly ? true : 'auto'"></v-select>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-list-item title="工作许可" style="color: gray;"></v-list-item>
                <v-img :src="workPermitImageURL" width="650px" min-height="100" @click="readonly ? true : selectImage()"></v-img>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <v-textarea label="违规记录" width="650px" density="compact" v-model="props.employment!.violation" :rules="[rules.maxLength128]" :readonly="readonly" :hide-details="readonly ? true : 'auto'"></v-textarea>
              </td>
            </tr>
          </tbody>
        </table>
      </v-form>
    </v-card-text>
    <v-card-actions v-if="showActions && !isLoading">
      <v-spacer></v-spacer>
      <v-btn v-if="readonly" @click="editStart()" :loading="isSaving">编辑</v-btn>
      <v-btn v-else @click="updateItem()" :loading="isSaving">保存</v-btn>
      <v-btn @click="isActive = false">关闭</v-btn>
    </v-card-actions>
  </v-card>
</template>