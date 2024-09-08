import React, { PureComponent } from 'react';
import { Form, Button, Input, Row, Col } from 'hzero-ui';

import intl from 'utils/intl';
import { Bind } from 'lodash-decorators';
import { SEARCH_FORM_ITEM_LAYOUT } from 'utils/constants';

const FormItem = Form.Item;

@Form.create({ fieldNameProp: null })
export default class FilterForm extends PureComponent {
  constructor(props) {
    super(props);
    props.onRef(this);
  }

  /**
   * 采购员列表条件查询
   */
  @Bind()
  handleSearch() {
    const { onSearch, form } = this.props;
    if (onSearch) {
      form.validateFields(err => {
        if (!err) {
          onSearch();
        }
      });
    }
  }

  /**
   * 采购员列表查询表单重置
   */
  @Bind()
  handleReset() {
    const { form } = this.props;
    form.resetFields();
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form>
        <Row type="flex" gutter={24} align="bottom">
          <Col span={8}>
            <FormItem
              {...SEARCH_FORM_ITEM_LAYOUT}
              label={intl
                .get('hpfm.purchaseAgent.model.purchaseAgent.purchaseAgentCode')
                .d('采购员编码')}
            >
              {getFieldDecorator('purchaseAgentCode', {
                initialValue: '',
              })(<Input trim typeCase="upper" inputChinese={false} />)}
            </FormItem>
          </Col>
          <Col span={8}>
            <FormItem
              {...SEARCH_FORM_ITEM_LAYOUT}
              label={intl
                .get('hpfm.purchaseAgent.model.purchaseAgent.purchaseAgentName')
                .d('采购员名称')}
            >
              {getFieldDecorator('purchaseAgentName', {
                initialValue: '',
              })(<Input />)}
            </FormItem>
          </Col>
          <Col span={8}>
            <FormItem>
              <Button onClick={this.handleReset}>
                {intl.get('hzero.common.button.reset').d('重置')}
              </Button>
              <Button
                type="primary"
                htmlType="submit"
                style={{ marginLeft: 8 }}
                onClick={this.handleSearch}
              >
                {intl.get('hzero.common.button.search').d('查询')}
              </Button>
            </FormItem>
          </Col>
        </Row>
      </Form>
    );
  }
}
