<template>
  <div class="help-content">
    <section class="content">
      <h2>{{ $t('help.design_concept_title') }}</h2>
      <ol>
        <li>{{ $t('help.design_concept_info_1') }}</li>
        <li>{{ $t('help.design_concept_info_2') }}</li>
      </ol>
      <h2>{{ $t('help.major_features_title') }}</h2>
      <ol>
        <li>{{ $t('help.major_features_info_1') }}</li>
        <li>{{ $t('help.major_features_info_2') }}</li>
        <li>{{ $t('help.major_features_info_3') }}</li>
        <li>{{ $t('help.major_features_info_4') }}</li>
        <li>{{ $t('help.major_features_info_5') }}</li>
      </ol>
      <h2>{{ $t('help.unsupported_title') }}</h2>
      <ol>
        <li>{{ $t('help.unsupported_info') }}</li>
      </ol>
    </section>
  </div>
</template>
<style>
  .help-content {
    padding-top: 10px;
  }
  .help-content li {
    list-style: inside;
  }
</style>
